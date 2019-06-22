from django.shortcuts import get_object_or_404, render

from django.views.decorators.csrf import requires_csrf_token
from django.http import HttpResponse, HttpResponseRedirect
from django.urls import reverse
from django.views import generic
from django.utils import timezone
from django.template import loader
from .models import SlackUser
from time import time
import math
from math import exp, sqrt, log
from random import gauss, seed
import scipy as sp
import numpy as np
import datetime
from .LeastSquareMontecarlo import LeastSquareMontecarlo
import os
import logging
import base64

from math import exp, sqrt, log
from random import gauss, seed

import os
import logging
import base64

logger = logging.getLogger("montecarlo_logger")

def login(request):

    template_name = 'montecarlo/login.html'
    return render(request, template_name)
def logout(request):
    template_name = 'montecarlo/login.html'
    return render(request, template_name)

@requires_csrf_token
def authenticate(request):
    print("authenticating")
    username = request.POST['useremail']
    password = request.POST['password']



    logger.info("authenticate username "+username )


    error_password = None
    try:
       user = get_object_or_404(SlackUser, username=username)
    except:
       template_name = 'montecarlo/login.html'
       error_username = "Invalid username"
       context = {'error_useremail': error_username,
                'error_password': error_password}
       return render(request, template_name,context)

    if user:
       check, error_username, error_password = user.authenticate(username, password)
       print(check,error_username,error_password)
       if check:

          template_name = 'montecarlo/main.html'
          logger.info("authenticated username "+username)

       else :
         print("setting template as login")
         template_name = 'montecarlo/login.html'
         logger.info("authenticate failure username "+username )

    else :
        print("setting template as login")
        template_name = 'montecarlo/login.html'
        error_username = "Invalid username"
        logger.info("validation failure username "+username )

    context = {'error_useremail': error_username,
                'error_password': error_password}

    return render(request, template_name,context)


def main(request):
    template_name = 'montecarlo/main.html'
    return render(request, template_name)

def signup(request):

    template_name = 'montecarlo/signup.html'
    return render(request, template_name)

def signin(request):
    username = request.POST['useremail']
    password = request.POST['password']
    confirmPassword = request.POST['confirmPassword']
    print("password, confirmPassword",password,confirmPassword)


    error_confirm_password = None
    error_username = None
    error_password = None

    error_username = _validate_username(username)
    error_password, error_confirm_password = _validate_password(password,confirmPassword)

    if error_username == None and error_password == None and error_confirm_password == None:
       if password == confirmPassword:
          user = SlackUser(username=username,password=password)
          user.save()

          template_name = 'montecarlo/login.html'
       else :
          template_name = 'montecarlo/signup.html'
    else :
            template_name = 'montecarlo/signup.html'

    context = {'error_confirm_password': error_confirm_password,
                'error_useremail': error_username,
                'error_password': error_password
                }
    return render(request, template_name,context)


def index(request):

    page,count = _parsePage(request)



    print("page", page)
    channels,nextCursor = slack.listChannelsPage(page,count)
    template_name = 'montecarlo/index.html'
    context = {'channels': channels,
                'nextCursor': nextCursor
                }
    return render(request, template_name, context)


def _validate_username(username):
    error_username = None
    if username == None:
       error_username = "user email is blank"

    if "@" not in username or "." not in username :
       error_username = "user email is not valid"
    return error_username


def euro_option(request):


    template_name = 'montecarlo/euro_option.html'

    return render(request, template_name)


def _validate_password(password,confirm_password):
    error_password = None
    error_confirm_password = None
    if password == None:
       error_password = "password is blank"
    if confirm_password == None:
       error_confirm_password = "confirm password is blank"
    if password != None and confirm_password != None:
       if password == confirm_password:
          error_password = None
          error_confirm_password = None
       else :
          error_password = "password and confirm_password do not match"
          error_confirm_password = "password and confirm_password do not match"
    return error_password, error_confirm_password

def euro_montecarlo(request):
  seed(20000)
  initial_time = time()
  template_name='montecarlo/euro_montecarlo.html'

  initial_value = float(request.POST["initial_value"])
  Strike_Price = float(request.POST["strike_price"])

  Maturity = float(request.POST["maturity"])
  risk = float(request.POST["risk"])

  volatility = float(request.POST["volatility"])

  Time_Steps= int(request.POST["time_steps"])
  num_paths = int(request.POST["num_paths"])

  dt = Maturity / Time_Steps

  Sim = []
  for i in range(num_paths):
    Simpath = []
    for t in range(Time_Steps + 1):
        if t == 0:
            Simpath.append(initial_value)
        else:
            z = gauss(0.0, 1.0)
            Simt = Simpath[t - 1] * exp((risk - 0.5 * volatility ** 2) * dt
                                  + volatility * sqrt(dt) * z)
            Simpath.append(Simt)
    Sim.append(Simpath)


  Option_Value = exp(-risk * Maturity) * sum([max(path[-1] - Strike_Price, 0) for path in Sim]) / num_paths

  time_taken = time() - initial_time

  context = {'option_value': Option_Value,
                'time_taken': time_taken}

  return render(request, template_name,context)

def asian_option(request):
    template_name = 'montecarlo/asian_option.html'

    return render(request, template_name)

def asian_montecarlo(request):
    seed(20000)
    initial_time = time()
    template_name='montecarlo/asian_montecarlo.html'

    s0 = float(request.POST["initial_value"])
    x = float(request.POST["exercise_price"])

    T = float(request.POST["maturity"])
    r = float(request.POST["risk"])

    sigma = float(request.POST["volatility"])

    n_simulation= int(request.POST["n_simulations"])
    n_steps = int(request.POST["n_steps"])
    sT=1
    total=0
    dt=T/n_steps
    call=sp.zeros([n_simulation],dtype=float)
    for j in range(0, n_simulation):
       sT*=s0
       total=0
       for i in range(0,int(n_steps)):
        	e=sp.random.normal()
        	sT*=sp.exp((r-0.5*sigma*sigma)*dt+sigma*e*sp.sqrt(dt))
        	total+=sT
       price_average=total/n_steps
       call[j]=max(price_average-x,0)
    call_price=np.mean(call)*exp(-r*T)

    cp=round(call_price,3)
    time_taken = time() - initial_time
    context = {'option_value': call_price,'time_taken': time_taken}

    return render(request, template_name,context)

def generate_asset_price(S,v,r,T):
    return S * exp((r - 0.5 * v**2) * T + v * sqrt(T) * gauss(0,1.0))

def call_payoff(S_T,K):
    return max(0.0,S_T-K)


def black_sholes_option(request):


    template_name = 'montecarlo/black_sholes_option.html'

    return render(request, template_name)

def black_sholes_montecarlo(request):
    initial_time = time()
    template_name='montecarlo/black_sholes_montecarlo.html'

    S = float(request.POST["underlying_price"])
    v = float(request.POST["volume"])
    T = (datetime.date(2013,9,21) - datetime.date(2013,9,3)).days / 365.0
    r = float(request.POST["rate"])
    K = float(request.POST["K"])

    simulations= int(request.POST["n_simulations"])

    payoffs = []
    discount_factor = math.exp(-r * T)

    for i in range(simulations):
        S_T = generate_asset_price(S,v,r,T)
        payoffs.append(
            call_payoff(S_T, K)
        )

    price = discount_factor * (sum(payoffs) / float(simulations))
    time_taken = time() - initial_time
    context = {'price': price,'time_taken': time_taken}
    return render(request,template_name,context)


def least_square_option(request):

    template_name = 'montecarlo/least_square_option.html'
    return render(request, template_name)

def least_square_montecarlo(request):

    S0 = float(request.POST["initial_stock_price"])
    T=float(request.POST["time_to_maturity"])
    strike = float(request.POST["strike"])
    M = float(request.POST["granularity_for_time"])
    r = float(request.POST["risk"])
    simulations=int(request.POST["n_simulations"])
    sigma = float(request.POST["volatility"])
    div=float(request.POST["dividend_yield"])

    template_name='montecarlo/least_square_montecarlo.html'
    t0 = time()

    AmericanPUT = LeastSquareMontecarlo('put', S0, strike, T, M, r, div, sigma, simulations)

    optionValues = AmericanPUT.price  # calculate all values
    t1 = time(); d1 = t1 - t0
    context={
    'optionValues':optionValues,
    'time':d1
    }

    return render(request,template_name,context)
