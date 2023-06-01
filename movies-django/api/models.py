from django.db import models


class Movie(models.Model):
    title = models.CharField(max_length=100)
    description = models.CharField(max_length=1000)
    owner = models.IntegerField(db_column="author")
    modification_date = models.DateTimeField(auto_now=True)
    url = models.CharField(max_length=100, default="")

    class Meta:
        db_table = "movies"


class Caption(models.Model):
    movie = models.ForeignKey(Movie, on_delete=models.CASCADE, related_name='captions')
    startTimeInSeconds = models.FloatField(db_column="start_time")
    durationInSeconds = models.FloatField(db_column="duration")
    text = models.CharField(max_length=100)
    modification_date = models.DateTimeField(auto_now=True)

    class Meta:
        db_table = "subtitles"


class Marker(models.Model):
    movie = models.ForeignKey(Movie, on_delete=models.CASCADE, related_name='markers')
    startTimeInSeconds = models.FloatField()
    durationInSeconds = models.FloatField()
    text = models.CharField(max_length=100)
    positionX = models.FloatField()
    positionY = models.FloatField()

    class Meta:
        db_table = "markers"
