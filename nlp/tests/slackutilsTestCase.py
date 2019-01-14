from django.test import TestCase
from django.conf import settings
from slackclient import SlackClient
import os
import logging
from nlp.slackutils import SlackUtil


class slackutilsTestCase(TestCase):

    def setUp(self): 
     self.slack_token = os.environ['SLACK_TOKEN']  

    def test_list_channels_page(self):


     slackUtil =  SlackUtil(self.slack_token) 

     channels,nextCursor = slackUtil.listChannelsPage(None,10)

     self.assertEqual(True,len(channels) >0) 

    def test_list_channels(self):


     slackUtil =  SlackUtil(self.slack_token) 

     channels = slackUtil.listChannels()
  
     self.assertEqual(True,len(channels) >0)      
     
    def test_search_All(self):


      slackUtil =  SlackUtil(self.slack_token) 

      messages,count = slackUtil.searchAll("day",1,10)

      self.assertEqual(True,len(messages) >0)

    def test_list_Messages_Page(self):


      slackUtil =  SlackUtil(self.slack_token) 

      channels,nextCursor = slackUtil.listChannelsPage(None,10)

      channelId = None

      for channel in channels:

     	 if channel["name"]== "random":
     	 	channelId = channel["id"]

      
      messages,count = slackUtil.listMessagesPage(channelId,None,10)

      self.assertEqual(True,len(messages) >0)

    def test_list_Messages(self):


      slackUtil =  SlackUtil(self.slack_token) 

      channels,nextCursor = slackUtil.listChannelsPage(None,10)

      channelId = None
 
      for channel in channels:

     	 if channel["name"]== "random":
     	 	channelId = channel["id"]
      #print("channel", channelName)

      messages = slackUtil.listMessages(channelId)

      #print("messages",messages)
     
      self.assertEqual(True,len(messages) >0)


