from django.urls import include, path

from api.views import HealthView

urlpatterns = [
    path('health.json', HealthView.as_view()),
    path('django/', include('api.urls'))
]
