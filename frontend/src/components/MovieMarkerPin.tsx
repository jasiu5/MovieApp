import React from "react";
import { IconButton, Popover, Typography } from "@mui/material";
import PushPinIcon from '@mui/icons-material/PushPin';

const MovieMarkerPin = (props: { marker: Marker }) => {
    const [anchorEl, setAnchorEl] = React.useState<HTMLButtonElement | null>(null);
    const videoElement = document.getElementById("video") as HTMLVideoElement | null;

    const handleOpen = (event: { currentTarget: React.SetStateAction<HTMLButtonElement | null> }) => {
        setAnchorEl(event?.currentTarget);
        if (videoElement !== null)
            videoElement.pause();
    }

    const handleClose = () => {
        setAnchorEl(null);
        if (videoElement !== null)
            videoElement.play();
    }

    return <>
        <IconButton style={{
            color: 'white',
            backgroundColor: 'rgba(0, 0, 0, 0.5)',
            position: "absolute",
            marginLeft: props.marker.positionX * (videoElement?.clientWidth || 1) / 100,
            marginTop: props.marker.positionY * (videoElement?.clientHeight || 1) / 100
        }}
            onClick={handleOpen}
        >
            <PushPinIcon fontSize="large" />
        </IconButton>
        <Popover
            open={Boolean(anchorEl)}
            anchorEl={anchorEl}
            onClose={handleClose}
            anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'left',
            }}
        >
            <Typography sx={{ p: 2 }}>{props.marker.text}</Typography>
        </Popover>
    </>
}

export default MovieMarkerPin;
