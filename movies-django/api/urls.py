from django.urls import path

from api.views import MovieView, MovieSearchView, MovieObjectView, LoginView, RegistrationView

urlpatterns = [
    path('movie/', MovieView.as_view()),
    path('movie/<int:pk>', MovieObjectView.as_view()),
    path('movie/search/', MovieSearchView.as_view()),
    path('login/', LoginView.as_view()),
    path('registration/', RegistrationView.as_view()),
]
