from django.contrib import admin

from .models import SlackUser


class SlackUserAdmin(admin.ModelAdmin):
	
    class Meta:
        model = SlackUser
 
admin.site.register(SlackUser)





