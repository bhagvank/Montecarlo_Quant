import datetime

from django.db import models
from django.utils import timezone
import os



class SlackUser(models.Model):
  
    
    username = models.CharField(max_length=200)
    password = models.CharField(max_length=200)


    def _str_(self):
     
      return self.username


    def authenticate(self,username,password):
      
        check = None
        error_username = self._validate_username(username)
        error_password = self._validate_password(password)
        if error_username == None and error_password == None:
           check = True
        else :
           check = False              
        return check,error_username,error_password

    def _validate_username(self,username):
        error_username = None
        if self.username != username:
           error_username = "InValid UserName" 

        return error_username

    def _validate_password(self, password):
        error_password = None
        if self.password != password: 
           error_password = "InValid Password"
        return error_password   


          
