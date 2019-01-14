from google.cloud import language
from google.oauth2 import service_account
from google.cloud.language import enums
from google.cloud.language import types
from django.conf import settings
from boto.s3.connection import S3Connection
import os

import json

class NLPUtil:
    """

     class for Google NLP API calls

    """

    def __init__(self):
        """
         non-parameterized constructor

        """

        self.data = []


    def analyseContentSentiment(self, messages):
        """
         analysing content sentiments given messages as parameter
        Parameters
        ----------
         messages : dictionary

        Returns
        ----------
         list
           list of message sentiments 

        """
        #s3 = S3Connection(os.environ['ACCESS_KEY_ID'], os.environ['SECRET_ACCESS_KEY'])
        #s3.Bucket('googleservicejson').download_file('/app/service.json', 'service.json')
        credentials = service_account.Credentials.from_service_account_file(os.environ['GOOGLE_SERVICE']
        )

        messageSentiments = []
        #print("messages analyse",messages)
        for key, value in messages.items():
            message = value
            client = language.LanguageServiceClient(credentials=credentials,)
            document = language.types.Document(
            content=message["text"],
            language='en',
            type=enums.Document.Type.PLAIN_TEXT,

             )
            response = client.analyze_sentiment(
             document=document, encoding_type='UTF8',)
            sentiment = response.document_sentiment
            userMessage = {}
            #userMessage["thread_ts"] = key
            userMessage["message"] = message
            userMessage["score"] = sentiment.score
            userMessage["magnitude"] = sentiment.magnitude
            messageSentiments.append(userMessage)
        # messageSentiments.append(sentiment)
        return messageSentiments

    def analyseEntities(self, messages):
        credentials = service_account.Credentials.from_service_account_file(os.environ['GOOGLE_SERVICE']
        )

        messageEntities = []
        #print("messages analyse",messages)
        for key, value in messages.items():
            message = value
            client = language.LanguageServiceClient(credentials=credentials,)
            document = language.types.Document(
            content=message["text"],
            language='en',
            type=enums.Document.Type.PLAIN_TEXT,

             )
            entities = client.analyze_entities(document).entities
             #document=document, encoding_type='UTF8',)
            #sentiment = response.document_sentiment
            userMessage = {}
            #userMessage["thread_ts"] = key
            userMessage["message"] = message
            userMessage["entities"] = entities
            
            entity_str = ""
            #message["entities"] = entities

            for entity in entities:
                if entity_str:
                   entity_str = entity_str + ","+ entity.name
                else:
                   entity_str =  entity.name   
            #message["entity_str"] = entity_str
            userMessage["entity_str"] = entity_str
            messageEntities.append(userMessage)
        # messageSentiments.append(sentiment)
        return messageEntities
    def analyseEntitySentiments(self, messages):
        credentials = service_account.Credentials.from_service_account_file(os.environ['GOOGLE_SERVICE']
        )

        entitySentiments = []
        #print("messages analyse",messages)
        for key, value in messages.items():
            message = value
            client = language.LanguageServiceClient(credentials=credentials,)
            document = language.types.Document(
            content=message["text"],
            language='en',
            type=enums.Document.Type.PLAIN_TEXT,

             )
            entities = client.analyze_entity_sentiment(document,enums.EncodingType.UTF32).entities
             #document=document, encoding_type='UTF8',)
            #sentiment = response.document_sentiment
            
            userMessage = {}
            #userMessage["thread_ts"] = key
            userMessage["message"] = message
            userMessage["entities"] = entities
            
            #entity_str = ""
            #message["entities"] = entities

            entity_str = ""
            #message["entities"] = entities
            for entity in entities:
                if entity_str:
                   entity_str = entity_str + ","+ entity.name +"{"+str(entity.sentiment)+"}"
                else:
                   entity_str = entity.name +"{"+str(entity.sentiment)+"}"
            #message["entity_str"] = entity_str
            userMessage["entity_str"] = entity_str
            entitySentiments.append(userMessage)
        return entitySentiments   
        # messageSentiments.app





