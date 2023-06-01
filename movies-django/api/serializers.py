import requests
from rest_framework import serializers

from api.models import Movie, Marker, Caption


class CaptionSerializer(serializers.ModelSerializer):
    class Meta:
        model = Caption
        fields = '__all__'


class MarkerSerializer(serializers.ModelSerializer):
    class Meta:
        model = Marker
        fields = '__all__'


class MovieSerializer(serializers.ModelSerializer):
    class Meta:
        model = Movie
        fields = '__all__'


class MovieDetailsSerializer(serializers.ModelSerializer):
    class Meta:
        model = Movie
        fields = '__all__'

    url = serializers.CharField(max_length=100, read_only=True)
    owner = serializers.IntegerField(read_only=True, required=False)
    captions = CaptionSerializer(many=True, read_only=True)
    markers = MarkerSerializer(many=True, read_only=True)

    def to_representation(self, instance):
        url = requests.get(f'http://localhost:8079/movies/{instance.url}').text
        repr = super().to_representation(instance)
        repr['url'] = url
        return repr
