import { Alert, FormControl, IconButton, InputAdornment, InputLabel, OutlinedInput, Typography } from "@mui/material";
import SearchIcon from '@mui/icons-material/Search';
import Skeleton from "@mui/material/Skeleton";
import Stack from "@mui/material/Stack";
import React, { useEffect, useState } from "react";
import useAPIFetch, { ENDPOINT } from '../useFetch';
import Box from "@mui/system/Box";
import { MovieListItem } from "../components";

const Home = () => {
    const [execute, status, data, isLoading] = useAPIFetch<MovieSearchResponseData>(ENDPOINT.MOVIE_SEARCH, "GET");
    const handleSearchClick = (event: React.MouseEvent) => {
        const searchValue = (document.getElementById("searchInput") as HTMLInputElement).value;

        execute({ filter: searchValue });
    }

    useEffect(() => {
        execute({});
    }, []);

    return <Box maxWidth='lg' margin='auto' pt="20px">
        <Typography variant="h4" p={2}>Wyszukiwanie filmów</Typography>
        <FormControl variant="outlined" fullWidth sx={{ mb: 3 }}>
            <InputLabel htmlFor="searchInput">Wyszukaj</InputLabel>
            <OutlinedInput
                id="searchInput"
                endAdornment={
                    <InputAdornment position="end">
                        <IconButton
                            onClick={handleSearchClick}
                            edge="end"
                        >
                            <SearchIcon />
                        </IconButton>
                    </InputAdornment>
                }
                label="Wyszukaj"
            />
        </FormControl>
        {
            isLoading ?
                <Stack spacing={1}>
                    {
                        [...Array(5)].map(i =>
                            <Skeleton key={i} variant="rectangular" width={"100%"} height={60} />)
                    }
                </Stack> :
                status === 200
                    ? (data.length === 0
                        ? <Alert severity="info" variant="filled">Nie znaleziono filmów spełniających podane wymagania</Alert>
                        : data.map(movie => <MovieListItem key={movie.id} movie={movie} />
                        ))
                    : <Alert severity="error" variant="filled">Wystąpił nieoczekiwany błąd</Alert>
        }
    </Box>;
}

export default Home;
