from django.db import models
from django.contrib.auth.models import User
from PIL import Image


class Profile(models.Model):
    user=models.OneToOneField(User,on_delete=models.CASCADE)
    image=models.ImageField(default='default.jpg',upload_to='profiles_pics')

    def __str__(self):
        return f'{self.user.username} Profile'

    def save(self,force_insert=False, force_update=False, using=None):
        super().save()

        img=Image.open(self.image.path)

        if img.height>250 or img.width>250:
            output_size=(250,250)
            img.thumbnail(output_size)
            img.save(self.image.path)
