import { Box, Typography, Stack, Skeleton, Alert, List, Button } from "@mui/material";
import React, { useEffect, useState } from "react";
import { MovieEditForm, MovieListItem } from "../components";
import useAPIFetch, { ENDPOINT } from "../useFetch";

const MoviesManagement = () => {
    const [execute, status, data, isLoading] = useAPIFetch<MovieSearchResponseData>(ENDPOINT.MOVIE_GET_POST);
    const [selectedMovieId, setSelectedMovieId] = useState<number | undefined>();
    const [moviesList, setMoviesList] = useState<Array<Movie>>([]);

    useEffect(() => {
        execute({}); // return own movies
    }, []);

    const handleEditFormSubmitCompleted = (movie: MovieDetails) => {
        if(!selectedMovieId) {
            setMoviesList([movie, ...moviesList]);
            setSelectedMovieId(movie.id);
        }
    }

    return <Box maxWidth='lg' margin='auto' pt="20px">
        <Typography variant="h4" p={2}>Moje filmy</Typography>
        <MovieEditForm onSubmitCompleted={handleEditFormSubmitCompleted} movieId={selectedMovieId}/>
        <Box p={3}>
        <Button onClick={() => setSelectedMovieId(undefined)} variant='contained' fullWidth sx={{mb: 3}} disabled={selectedMovieId === undefined}>Dodaj nowy film</Button>
            {
                isLoading ?
                    <Stack spacing={1}>
                        {
                            [...Array(5)].map(i =>
                                <Skeleton key={i} variant="rectangular" width={"100%"} height={90} />)
                        }
                    </Stack> :
                    status === 200
                        ? (data.length === 0
                            ? <Alert severity="info" variant="filled">Nie dodałeś żadnych filmów</Alert>
                            : <List>{data.map(movie => <MovieListItem key={movie.id} selected={movie.id === selectedMovieId} movie={movie} onclick={() => { setSelectedMovieId(movie.id) }} />)}</List>
                        )
                        : <Alert severity="error" variant="filled">Wystąpił nieoczekiwany błąd</Alert>
            }
        </Box>
    </Box>;
}

export default MoviesManagement;
