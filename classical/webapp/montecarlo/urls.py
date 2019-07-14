from django.urls import path

from . import views



urlpatterns = [

    path('euro_option/', views.euro_option, name='euro_option'),
    path('euro_montecarlo/', views.euro_montecarlo, name='euro_montecarlo'),
    path('asian_montecarlo/', views.asian_montecarlo, name='asian_montecarlo'),
    path('asian_option/', views.asian_option, name='asian_option'),
    path('black_sholes_montecarlo/', views.black_sholes_montecarlo, name='black_sholes_montecarlo'),
    path('black_sholes_option/', views.black_sholes_option, name='black_sholes_option'),
    path('least_square_montecarlo/', views.least_square_montecarlo, name='least_square_montecarlo'),
    path('least_square_option/', views.least_square_option, name='least_square_option'),
    path('main/', views.main, name='main'),
    path('euro_option/', views.euro_option, name='euro_option'),
    path('euro_montecarlo/', views.euro_montecarlo, name='euro_montecarlo'),
    path('main/', views.main, name='main'),

]
