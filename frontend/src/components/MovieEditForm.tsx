import React, { useEffect, useReducer } from "react";
import { Alert, Button, Container, Fab, Grid, IconButton, List, ListItem, TextField, Typography } from "@mui/material";
import Box from "@mui/material/Box";
import useAPIFetch, { ENDPOINT } from "../useFetch";
import MovieEditFormSkeleton from "./MovieEditFormSkeleton";
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';

type reducerActions = {
    type: 'addCaption' | 'addMarker' | 'reset'
} | {
    type: 'updateCaption' | 'updateMarker',
    index: number,
    value: string,
    name: string
} | {
    type: 'deleteCaption' | 'deleteMarker',
    index: number
} | {
    type: 'fetchResult',
    data: MovieDetails
} | {
    type: 'updateTitle',
    value: string
} | {
    type: 'updateDescription',
    value: string
}

const reducer = (state: MovieDetails, action: reducerActions): MovieDetails => {
    switch (action.type) {
        case 'updateTitle': {
            state.title = action.value;
            return { ...state };
        }
        case 'updateDescription': {
            state.description = action.value;
            return { ...state };
        }
        case 'addCaption': {
            state.captions.push({ startTimeInSeconds: 0, durationInSeconds: 0, text: '' });
            return { ...state };
        }
        case 'updateCaption': {
            state.captions[action.index] = {
                startTimeInSeconds: action.name === 'startTime' ? parseInt(action.value) : state.captions[action.index].startTimeInSeconds,
                durationInSeconds: action.name === 'duration' ? parseInt(action.value) : state.captions[action.index].durationInSeconds,
                text: action.name === 'text' ? action.value : state.captions[action.index].text
            }
            return { ...state };
        }
        case 'deleteCaption': {
            state.captions.splice(action.index, 1);
            return { ...state };
        }
        case 'addMarker': {
            state.markers.push({ startTimeInSeconds: 0, durationInSeconds: 0, text: '', positionX: 0, positionY: 0 });
            return { ...state };
        }
        case 'updateMarker': {
            const marker = state.markers[action.index];
            state.markers[action.index] = {
                startTimeInSeconds: action.name === 'startTime' ? parseInt(action.value) : marker.startTimeInSeconds,
                durationInSeconds: action.name === 'duration' ? parseInt(action.value) : marker.durationInSeconds,
                text: action.name === 'text' ? action.value : marker.text,
                positionX: action.name === 'X' ? parseInt(action.value) : marker.positionX,
                positionY: action.name === 'Y' ? parseInt(action.value) : marker.positionY
            }
            return { ...state };
        }
        case 'deleteMarker': {
            state.markers.splice(action.index, 1);
            return { ...state };
        }
        case 'reset': {
            return { title: '', description: '', url: '', captions: [], markers: [] };
        }
        case 'fetchResult': {
            return action.data;
        }
        default: {
            return { ...state };
        }
    }
}

const MovieEditForm = (props: { movieId?: number, onSubmitCompleted: (data: MovieDetails) => void }) => {
    const [execute, status, data, isLoading] = useAPIFetch<MovieDetails>(ENDPOINT.MOVIE_GET_POST);
    const [executeSubmit, statusSubmit, dataSubmit, isLoadingSubmit] = useAPIFetch<MovieDetails>(ENDPOINT.MOVIE_GET_POST, props.movieId ? "PATCH" : "POST", props.movieId === undefined);

    useEffect(() => {
        if (props.movieId === undefined) {
            dispatch({ type: 'reset' });
        } else {
            execute({}, props.movieId);
        }
    }, [props.movieId]);

    useEffect(() => {
        if ((status === 200 || status === 201) && !isLoading) {
            dispatch({ type: 'fetchResult', data: data });
        }
    }, [data, status]);

    const handleSubmit = (event: { preventDefault: () => void; currentTarget: HTMLFormElement | undefined; }) => {
        event.preventDefault();

        if (props.movieId) executeSubmit(state, props.movieId);
        else {
            const formData = new FormData(event.currentTarget);
            formData.append("title", state.title);
            formData.append("description", state.description);
            formData.append("captions", JSON.stringify(state.captions));
            formData.append("markers", JSON.stringify(state.markers));

            executeSubmit(formData);
        };
    }

    useEffect(() => {
        if (!isLoading && statusSubmit === 200) {
            props.onSubmitCompleted(dataSubmit);
        }
    }, [statusSubmit, isLoadingSubmit]);

    const [state, dispatch] = useReducer<(state: MovieDetails, actions: reducerActions) => MovieDetails>(reducer, { title: '', description: '', url: '', captions: [], markers: [] });

    return <Container maxWidth='lg'>
        <Box>
            {
                isLoading
                    ? <MovieEditFormSkeleton />
                    : <Box component="form" onSubmit={handleSubmit}>
                        {!isLoadingSubmit && statusSubmit === 400 && <Alert severity="error">Coś poszło nie tak</Alert>}
                        {!isLoadingSubmit && statusSubmit === 200 && <Alert>Zapisano</Alert>}
                        <TextField
                            fullWidth
                            required
                            margin="normal"
                            label="Tytuł"
                            onChange={({ currentTarget }) => dispatch({ type: 'updateTitle', value: currentTarget.value })}
                            value={state.title}
                        />
                        <TextField
                            fullWidth
                            required
                            margin="normal"
                            label="Opis"
                            multiline
                            minRows={3}
                            onChange={({ currentTarget }) => dispatch({ type: 'updateDescription', value: currentTarget.value })}
                            value={state.description}
                        />
                        {
                            props.movieId === undefined &&
                            <Button variant="contained" component="label">
                                Wybierz film
                                <input hidden accept="video/*" type="file" name='file' required />
                            </Button>
                        }
                        <Grid container>
                            <Grid xs={6} p={2} item>
                                <Typography variant="h6">Napisy</Typography>
                                <List>
                                    {
                                        state.captions.map((caption, idx) => <ListItem
                                            secondaryAction={
                                                <IconButton edge="end" aria-label="delete" onClick={() => dispatch({ type: 'deleteCaption', index: idx })}>
                                                    <DeleteIcon />
                                                </IconButton>
                                            }
                                        >
                                            <TextField
                                                fullWidth
                                                required
                                                margin="normal"
                                                label="Napis"
                                                name="text"
                                                value={caption.text}
                                                onChange={({ currentTarget }) => dispatch({ type: 'updateCaption', index: idx, name: 'text', value: currentTarget.value })}
                                            />
                                            <TextField
                                                fullWidth
                                                required
                                                margin="normal"
                                                type="number"
                                                label="Czas trwania (sec)"
                                                name="durationInSeconds"
                                                value={caption.durationInSeconds}
                                                onChange={({ currentTarget }) => dispatch({ type: 'updateCaption', index: idx, name: 'duration', value: currentTarget.value })}
                                            />
                                            <TextField
                                                fullWidth
                                                required
                                                margin="normal"
                                                label="Czas rozpoczęcia"
                                                name="startTimeInSeconds"
                                                type="number"
                                                value={caption.startTimeInSeconds}
                                                onChange={({ currentTarget }) => dispatch({ type: 'updateCaption', index: idx, name: 'startTime', value: currentTarget.value })}
                                            />
                                        </ListItem>)
                                    }

                                </List>
                                <Fab color="primary" onClick={() => dispatch({ type: 'addCaption' })}>
                                    <AddIcon />
                                </Fab>
                            </Grid>
                            <Grid xs={6} p={2} item>
                                <Typography variant="h6">Markery</Typography>
                                <List>
                                    {
                                        state.markers.map((marker, idx) => <ListItem
                                            secondaryAction={
                                                <IconButton edge="end" aria-label="delete" onClick={() => dispatch({ type: 'deleteMarker', index: idx })}>
                                                    <DeleteIcon />
                                                </IconButton>
                                            }
                                        >
                                            <TextField
                                                fullWidth
                                                required
                                                margin="normal"
                                                label="Napis"
                                                name="text"
                                                defaultValue={marker.text}
                                                onChange={({ currentTarget }) => dispatch({ type: 'updateMarker', index: idx, name: 'text', value: currentTarget.value })}
                                            />
                                            <TextField
                                                fullWidth
                                                required
                                                margin="normal"
                                                type="number"
                                                label="Czas trwania (sec)"
                                                name="durationInSeconds"
                                                defaultValue={marker.durationInSeconds}
                                                onChange={({ currentTarget }) => dispatch({ type: 'updateMarker', index: idx, name: 'duration', value: currentTarget.value })}
                                            />
                                            <TextField
                                                fullWidth
                                                required
                                                margin="normal"
                                                label="Czas rozpoczęcia"
                                                name="startTimeInSeconds"
                                                type="number"
                                                defaultValue={marker.startTimeInSeconds}
                                                onChange={({ currentTarget }) => dispatch({ type: 'updateMarker', index: idx, name: 'startTime', value: currentTarget.value })}
                                            />
                                            <TextField
                                                fullWidth
                                                required
                                                margin="normal"
                                                label="Poziom"
                                                name="positionX"
                                                type="number"
                                                defaultValue={marker.positionX}
                                                onChange={({ currentTarget }) => dispatch({ type: 'updateMarker', index: idx, name: 'X', value: currentTarget.value })}
                                            />
                                            <TextField
                                                fullWidth
                                                required
                                                margin="normal"
                                                label="Pion"
                                                name="positionY"
                                                type="number"
                                                defaultValue={marker.positionY}
                                                onChange={({ currentTarget }) => dispatch({ type: 'updateMarker', index: idx, name: 'Y', value: currentTarget.value })}
                                            />
                                        </ListItem>)
                                    }
                                </List>
                                <Fab color="primary" onClick={() => dispatch({ type: 'addMarker' })}>
                                    <AddIcon />
                                </Fab>
                            </Grid>
                        </Grid>
                        <Button
                            fullWidth
                            sx={{ mt: 3 }}
                            variant="contained"
                            type="submit"
                            disabled={isLoadingSubmit}>
                            {
                                isLoading ? "Ładowanie..." : "Zapisz"
                            }
                        </Button>
                    </Box>
            }
        </Box>
    </Container>
}

export default MovieEditForm;
