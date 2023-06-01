from rest_framework import authentication
import requests


class TokenAuthentication(authentication.BaseAuthentication):
    def authenticate(self, request):
        try:
            key = self.retrieve_token_from_request(request)
        except (TypeError, AttributeError, IndexError):
            return None, None

        response = requests.post('http://localhost:8079/usertoken', json={'token': key})
        try:
            return response.json()['id'], None
        except BaseException as e:
            return None, None

    def retrieve_token_from_request(self, request):
        return request.META.get('HTTP_AUTHORIZATION').split()[1]
