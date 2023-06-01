import React from "react";
import LoginPage from "./login";
import RegistrationPage from "./registration";
import Home from "./home";
import MoviePage from "./movie";
import MoviesManagement from "./moviesManagement";

const Pages = {
    SIGNIN: {
        path: '/signin',
        name: 'Logowanie',
        component: <LoginPage />
    },
    SIGNUP: {
        path: '/signup',
        name: 'Rejestracja',
        component: <RegistrationPage />
    },
    HOME: {
        path: '/',
        name: 'Strona domowa',
        component: <Home />
    },
    MOVIE: {
        path: '/movie',
        name: 'Film',
        component: <MoviePage />
    },
    MY_MOVIES: {
        path: '/myMovies',
        name: 'Moje filmy',
        component: <MoviesManagement />
    }
}

export default Pages;
