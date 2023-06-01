import { Card, CardContent, Typography, CardActionArea, ListItemButton } from "@mui/material";
import React from "react";
import { useNavigate } from "react-router-dom";
import Pages from "../pages";

const MovieListItem = (props: { movie: Movie, onclick?: () => void, [x: string]: any }) => {
    const navigate = useNavigate();
    const { id, description, title } = props.movie;

    return <ListItemButton onClick={props.onclick ? props.onclick : () => navigate(Pages.MOVIE.path, { state: { id: id } })} {...props.x} fullWidth>
        <Card sx={{ width: '100%' }}>
            <CardActionArea>
                <CardContent>
                    <Typography variant="h5" component="div" gutterBottom>
                        {title}
                    </Typography>
                    <Typography color="text.secondary">
                        {description}
                    </Typography>
                </CardContent>
            </CardActionArea>
        </Card>
    </ListItemButton>
}

export default MovieListItem;
