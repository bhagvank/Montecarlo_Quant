from django.test import TestCase
from django.conf import settings
from slackclient import SlackClient
import os
import logging
from nlp.NLPUtils import NLPUtil
from nlp.slackutils import SlackUtil

class nlputilsTestCase(TestCase):

    def setUp(self): 
     self.slack_token = os.environ['SLACK_TOKEN']
     
     


    def test_analyse_sentiments(self):
     
     #credentials = service_account.Credentials.from_service_account_file(os.environ['GOOGLE_SERVICE'])
     slackUtil =  SlackUtil(self.slack_token) 

     channels,nextCursor = slackUtil.listChannelsPage(None,10)

     channelId = None
 
     for channel in channels:

     	if channel["name"]== "random":
     	 channelId = channel["id"]
      #print("channel", channelName)

     messages = slackUtil.listMessages(channelId)
     nlpUtil = NLPUtil()
     messageSentiments = nlpUtil.analyseContentSentiment(messages);
     self.assertEqual(True,len(messageSentiments) >0)

    def test_analyse_entities(self):
     
     #credentials = service_account.Credentials.from_service_account_file(os.environ['GOOGLE_SERVICE'])
     slackUtil =  SlackUtil(self.slack_token) 

     channels,nextCursor = slackUtil.listChannelsPage(None,10)

     channelId = None
 
     for channel in channels:

     	if channel["name"]== "random":
     	 channelId = channel["id"]
      #print("channel", channelName)

     messages = slackUtil.listMessages(channelId)
     nlpUtil = NLPUtil()
     messageEntities = nlpUtil.analyseEntities(messages);
     self.assertEqual(True,len(messageEntities) >0)

    def test_analyse_entity_sentiments(self):
     
     #credentials = service_account.Credentials.from_service_account_file(os.environ['GOOGLE_SERVICE'])
     slackUtil =  SlackUtil(self.slack_token) 

     channels,nextCursor = slackUtil.listChannelsPage(None,10)

     channelId = None
 
     for channel in channels:

     	if channel["name"]== "random":
     	 channelId = channel["id"]
      #print("channel", channelName)

     messages = slackUtil.listMessages(channelId)
     nlpUtil = NLPUtil()
     entitySentiments = nlpUtil.analyseEntities(messages);
     self.assertEqual(True,len(entitySentiments) >0) 
     