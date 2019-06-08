from django.shortcuts import get_object_or_404, render


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
#    print ('call price = ', round(call_price,3))
    cp=round(call_price,3)
    time_taken = time() - initial_time
    context = {'option_value': call_price,'time_taken': time_taken}

    return render(request, template_name,context)

def generate_asset_price(S,v,r,T):
    return S * exp((r - 0.5 * v**2) * T + v * sqrt(T) * gauss(0,1.0))

def call_payoff(S_T,K):
    return max(0.0,S_T-K)

# S = 857.29 # underlying price
# v = 0.2076 # vol of 20.76%
# r = 0.0014 # rate of 0.14%

# K = 860.
# simulations = 90000

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

#    r = float(request.POST["risk"])

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

class AmericanOptionsLSMC(object):
    """ Class for American options pricing using Longstaff-Schwartz (2001):
    "Valuing American Options by Simulation: A Simple Least-Squares Approach."
    Review of Financial Studies, Vol. 14, 113-147.
    S0 : float : initial stock/index level
    strike : float : strike price
    T : float : time to maturity (in year fractions)
    M : int : grid or granularity for time (in number of total points)
    r : float : constant risk-free short rate
    div :    float : dividend yield
    sigma :  float : volatility factor in diffusion term

    Unitest(doctest):
    >>> AmericanPUT = AmericanOptionsLSMC('put', 36., 40., 1., 50, 0.06, 0.06, 0.2, 10000  )
    >>> AmericanPUT.price
    4.4731177017712209
    """

    def __init__(self, option_type, S0, strike, T, M, r, div, sigma, simulations):
        try:
            self.option_type = option_type
            assert isinstance(option_type, str)
            self.S0 = float(S0)
            self.strike = float(strike)
            assert T > 0
            self.T = float(T)
            assert M > 0
            self.M = int(M)
            assert r >= 0
            self.r = float(r)
            assert div >= 0
            self.div = float(div)
            assert sigma > 0
            self.sigma = float(sigma)
            assert simulations > 0
            self.simulations = int(simulations)
        except ValueError:
            print('Error passing Options parameters')


        if option_type != 'call' and option_type != 'put':
            raise ValueError("Error: option type not valid. Enter 'call' or 'put'")
        if S0 < 0 or strike < 0 or T <= 0 or r < 0 or div < 0 or sigma < 0:
            raise ValueError('Error: Negative inputs not allowed')

        self.time_unit = self.T / float(self.M)
        self.discount = np.exp(-self.r * self.time_unit)

    @property
    def MCprice_matrix(self, seed = 123):
        """ Returns MC price matrix rows: time columns: price-path simulation """
        np.random.seed(seed)
        MCprice_matrix = np.zeros((self.M + 1, self.simulations), dtype=np.float64)
        MCprice_matrix[0,:] = self.S0
        for t in xrange(1, self.M + 1):
            brownian = np.random.standard_normal( self.simulations / 2)
            brownian = np.concatenate((brownian, -brownian))
            MCprice_matrix[t, :] = (MCprice_matrix[t - 1, :]
                                  * np.exp((self.r - self.sigma ** 2 / 2.) * self.time_unit
                                  + self.sigma * brownian * np.sqrt(self.time_unit)))
        return MCprice_matrix

    @property
    def MCpayoff(self):
        """Returns the inner-value of American Option"""
        if self.option_type == 'call':
            payoff = np.maximum(self.MCprice_matrix - self.strike,
                           np.zeros((self.M + 1, self.simulations),dtype=np.float64))
        else:
            payoff = np.maximum(self.strike - self.MCprice_matrix,
                            np.zeros((self.M + 1, self.simulations),
                            dtype=np.float64))
        return payoff

    @property
    def value_vector(self):
        value_matrix = np.zeros_like(self.MCpayoff)
        value_matrix[-1, :] = self.MCpayoff[-1, :]
        for t in range(self.M - 1, 0 , -1):
            regression = np.polyfit(self.MCprice_matrix[t, :], value_matrix[t + 1, :] * self.discount, 5)
            continuation_value = np.polyval(regression, self.MCprice_matrix[t, :])
            value_matrix[t, :] = np.where(self.MCpayoff[t, :] > continuation_value,
                                          self.MCpayoff[t, :],
                                          value_matrix[t + 1, :] * self.discount)

        return value_matrix[1,:] * self.discount


    @property
    def price(self): return np.sum(self.value_vector) / float(self.simulations)

    @property
    def delta(self):
        diff = self.S0 * 0.01
        myCall_1 = AmericanOptionsLSMC(self.option_type, self.S0 + diff,
                                       self.strike, self.T, self.M,
                                       self.r, self.div, self.sigma, self.simulations)
        myCall_2 = AmericanOptionsLSMC(self.option_type, self.S0 - diff,
                                       self.strike, self.T, self.M,
                                       self.r, self.div, self.sigma, self.simulations)
        return (myCall_1.price - myCall_2.price) / float(2. * diff)

    @property
    def gamma(self):
        diff = self.S0 * 0.01
        myCall_1 = AmericanOptionsLSMC(self.option_type, self.S0 + diff,
                                       self.strike, self.T, self.M,
                                       self.r, self.div, self.sigma, self.simulations)
        myCall_2 = AmericanOptionsLSMC(self.option_type, self.S0 - diff,
                                       self.strike, self.T, self.M,
                                       self.r, self.div, self.sigma, self.simulations)
        return (myCall_1.delta - myCall_2.delta) / float(2. * diff)

    @property
    def vega(self):
        diff = self.sigma * 0.01
        myCall_1 = AmericanOptionsLSMC(self.option_type, self.S0,
                                       self.strike, self.T, self.M,
                                       self.r, self.div, self.sigma + diff,
                                       self.simulations)
        myCall_2 = AmericanOptionsLSMC(self.option_type, self.S0,
                                       self.strike, self.T, self.M,
                                       self.r, self.div, self.sigma - diff,
                                       self.simulations)
        return (myCall_1.price - myCall_2.price) / float(2. * diff)

    @property
    def rho(self):
        diff = self.r * 0.01
        if (self.r - diff) < 0:
            myCall_1 = AmericanOptionsLSMC(self.option_type, self.S0,
                                       self.strike, self.T, self.M,
                                       self.r + diff, self.div, self.sigma,
                                       self.simulations)
            myCall_2 = AmericanOptionsLSMC(self.option_type, self.S0,
                                       self.strike, self.T, self.M,
                                       self.r, self.div, self.sigma,
                                       self.simulations)
            return (myCall_1.price - myCall_2.price) / float(diff)
        else:
            myCall_1 = AmericanOptionsLSMC(self.option_type, self.S0,
                                       self.strike, self.T, self.M,
                                       self.r + diff, self.div, self.sigma,
                                       self.simulations)
            myCall_2 = AmericanOptionsLSMC(self.option_type, self.S0,
                                       self.strike, self.T, self.M,
                                       self.r - diff, self.div, self.sigma,
                                       self.simulations)
            return (myCall_1.price - myCall_2.price) / float(2. * diff)

    @property
    def theta(self):
        diff = 1 / 252.
        myCall_1 = AmericanOptionsLSMC(self.option_type, self.S0,
                                       self.strike, self.T + diff, self.M,
                                       self.r, self.div, self.sigma,
                                       self.simulations)
        myCall_2 = AmericanOptionsLSMC(self.option_type, self.S0,
                                       self.strike, self.T - diff, self.M,
                                       self.r, self.div, self.sigma,
                                       self.simulations)
        return (myCall_2.price - myCall_1.price) / float(2. * diff)

def least_square_option(request):


    template_name = 'montecarlo/least_square_option.html'

    return render(request, template_name)

def least_square_montecarlo():
    S0 = float(request.POST["initial_stock_price"])
#    vol = float(request.POST["volatility"])
    T=float(request.POST["time_to_maturity"])

    strike = float(request.POST["strike"])

    M = float(request.POST["maturity"])
    r = float(request.POST["risk"])
    simulations=float(request.POST["n_simulations"])
    sigma = float(request.POST["volatilitys"])
    div=float(request.POST["div"])

    # Time_Steps= int(request.POST["time_steps"])
    # num_paths = int(request.POST["num_paths"])

    template_name='montecarlo/least_square_montecarlo.html'
    t0 = time()
    # self, option_type, S0, strike, T, M, r, div, sigma, simulations
    # 'put', S0, 40., T, 50, 0.06, 0.06, vol, 1500
    # for S0 in (36., 38., 40., 42., 44.):  # initial stock price values
    #     for vol in (0.2, 0.4):  # volatility values
    #         for T in (1.0, 2.0):  # times-to-maturity
    AmericanPUT = AmericanOptionsLSMC('put', S0, strike, T, M, r, div, sigma, simulations)
    #print "Initial price: %4.1f, Sigma: %4.2f, Expire: %2.1f --> Option Value %8.3f" % (S0, vol, T, AmericanPUT.price)


    optionValues = least_square_montecarlo()  # calculate all values
    t1 = time(); d1 = t1 - t0
    context={
    'AmericanOptionsLSMC':AmericanOptionsLSMC,
    'time':d1
    }

    return render(request,template_name,context)
#    print "Duration in Seconds %6.3f" % d1
