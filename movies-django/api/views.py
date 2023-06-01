from rest_framework import generics
from rest_framework.response import Response
from rest_framework.views import APIView

from api.auth import TokenAuthentication
from api.models import Movie
from api.serializers import MovieSerializer, MovieDetailsSerializer, CaptionSerializer, MarkerSerializer
import requests
import json


class MovieView(generics.ListCreateAPIView):
    serializer_class = MovieSerializer
    queryset = Movie.objects.all()
    authentication_classes = [TokenAuthentication]

    def post(self, request, *args, **kwargs):
        captions = json.loads(request.data.pop('captions')[0])
        markers = json.loads(request.data.pop('markers')[0])
        file = request.FILES.pop('file')[0]

        movie_serializer = MovieDetailsSerializer(data=request.data)
        movie_serializer.is_valid(raise_exception=True)
        movie_serializer.save(owner=request.user)
        movie = movie_serializer.instance

        for caption in captions:
            s = CaptionSerializer(data=caption | {'movie': movie.pk})

            s.is_valid(raise_exception=True)
            s.save()

        for marker in markers:
            s = MarkerSerializer(data=marker | {'movie': movie.pk})
            s.is_valid(raise_exception=True)
            s.save()

        name_to_upload = f"{movie.pk}.{file.name.split('.')[-1]}"
        file.name = name_to_upload
        response = requests.post('http://localhost:8079/movies/add_movie',
                                 files=[('file', (name_to_upload, file.read()))])

        if response.ok:
            movie.url = name_to_upload
            movie.save()
            return Response(data=MovieDetailsSerializer(movie).data, status=201)
        else:
            movie.delete()
        return Response(status=400, data={'details': 'Error during saving file'})

    def filter_queryset(self, queryset):
        return queryset.filter(owner=self.request.user)


class MovieObjectView(generics.RetrieveUpdateDestroyAPIView):
    authentication_classes = [TokenAuthentication]

    serializer_class = MovieDetailsSerializer
    queryset = Movie.objects.all()

    def patch(self, request, *args, **kwargs):
        movie = self.get_object()

        captions = request.data.pop('captions')
        markers = request.data.pop('markers')

        captions_serializers = []
        markers_serializers = []

        movie_serializer = MovieDetailsSerializer(movie, data=request.data)
        movie_serializer.is_valid(raise_exception=True)

        for caption in captions:
            s = CaptionSerializer(data=caption | {'movie': movie.pk})
            s.is_valid(raise_exception=True)
            captions_serializers.append(s)

        for marker in markers:
            s = MarkerSerializer(data=marker | {'movie': movie.pk})
            s.is_valid(raise_exception=True)
            markers_serializers.append(s)

        movie_serializer.save()

        movie.captions.all().delete()
        movie.markers.all().delete()

        for s in markers_serializers:
            s.save()

        for s in captions_serializers:
            s.save()

        return Response(data=MovieDetailsSerializer(movie).data, status=200)


class MovieSearchView(generics.ListAPIView):
    authentication_classes = [TokenAuthentication]

    serializer_class = MovieSerializer
    queryset = Movie.objects.all()

    def filter_queryset(self, queryset):
        value = self.request.query_params.get('filter', '')
        response = requests.post('http://localhost:8079/search', data=value, headers={
            'Content-Type': 'application/json'
        })

        print(response.status_code)

        if response.status_code == 200:
            return queryset.filter(id__in=response.json())
        return queryset


class LoginView(APIView):
    authentication_classes = [TokenAuthentication]

    def post(self, request):
        response = requests.post('http://localhost:8079/login',
                                 json={'email': request.data.get('email'), 'password': request.data.get('password')})

        if response.status_code == 200 and response.text != '':
            return Response(data={'token': response.text})
        else:
            return Response(status=400, data=response.text)


class RegistrationView(APIView):
    authentication_classes = [TokenAuthentication]

    def post(self, request):
        response = requests.post('http://localhost:8079/registration',
                                 json={'email': request.data.get('email'), 'password': request.data.get('password'),
                                       'name': request.data.get('name')})

        if response.status_code == 200 and response.text != '':
            return Response(data={'token': response.text})
        else:
            return Response(status=400, data=response.text)


class HealthView(APIView):
    def get(self, request):
        return Response(data={'status': 'UP'})
