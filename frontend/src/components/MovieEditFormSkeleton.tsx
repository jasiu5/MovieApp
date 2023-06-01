import { Box, Grid, List, ListItem, Skeleton, Typography } from "@mui/material";
import React from "react";

const MovieEditFormSkeleton = () => {
    return <Box component="form">
        <Skeleton />
        <Skeleton />
        <Grid container>
            <Grid xs={6} p={2} item>
                <Typography variant="h6">Napisy</Typography>
                <List>
                    <ListItem>
                        <Skeleton />
                        <Skeleton />
                        <Skeleton />
                    </ListItem>
                </List>
            </Grid>
            <Grid xs={6} p={2} item>
                <Typography variant="h6">Markery</Typography>
                <List>
                    <ListItem>
                        <Skeleton />
                        <Skeleton />
                        <Skeleton />
                    </ListItem>
                </List>
            </Grid>
        </Grid>
    </Box>
}

export default MovieEditFormSkeleton;
