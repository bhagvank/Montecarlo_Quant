import datetime

from django.db import models
from django.utils import timezone
import os

# Create your models here.


class SlackUser(models.Model):
    """
    Slack User models

    """
    
    username = models.CharField(max_length=200)
    password = models.CharField(max_length=200)
    slacktoken = "abc"

    def _str_(self):
      """
       str method
       Returns
      -----------
       str
         username
      """
      return self.username

    def getSlackToken(self):
      """
       str method
       Returns
      -----------
       str
         username
      """
      return self.slacktoken

    def authenticate(self,username,password):
        """
        authenticate method
        Parameters
        ----------
        username : str
         user name
        password : str
           password  
         Returns
         -----------
          bool
           true if authentication is successful and false if it fails.
         """

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


          
