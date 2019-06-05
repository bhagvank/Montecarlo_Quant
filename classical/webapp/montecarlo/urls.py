from django.urls import path

from . import views



urlpatterns = [

    path('', views.login, name='login'),
    path('authenticate/', views.authenticate, name='authenticate'),
    path('euro_option/', views.euro_option, name='euro_option'),
    path('euro_montecarlo/', views.euro_montecarlo, name='euro_montecarlo'),
    path('asian_montecarlo/', views.asian_montecarlo, name='asian_montecarlo'),
    path('asian_option/', views.asian_option, name='asian_option'),
    path('signin/', views.signin, name='signin'),
    path('main/', views.main, name='main'),
    path('index/', views.index, name='index'),
    path('signup/', views.signup, name='signup'),
    path('logout/', views.logout, name='logout'),

]
