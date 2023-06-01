import { Alert, Skeleton, Typography } from "@mui/material";
import Box from "@mui/material/Box";
import React, { useEffect, useRef, useState } from "react";
import { useLocation } from "react-router-dom";
import { MovieMarkerPin } from "../components";
import useAPIFetch, { ENDPOINT } from "../useFetch";

const MoviePage = () => {
    const [captionsToShow, setCaptionsToShow] = useState<Array<Caption>>([]);
    const [markersToShow, setMarkersToShow] = useState<Array<Marker>>([]);

    const { state: { id } } = useLocation();
    const [execute, status, data, isLoading] = useAPIFetch<MovieGetResponseData>(ENDPOINT.MOVIE_GET_POST);

    useEffect(() => {
        execute({}, id);
    }, []);

    useEffect(() => {
        const interval = setInterval(() => {
            const videoElement = document.getElementById("video") as HTMLVideoElement | null;
            console.log(data);
            if (videoElement === null) return; 
            if (data === undefined) return;

            const currentTime = videoElement.currentTime;
            setCaptionsToShow(data.captions.filter(({ durationInSeconds, startTimeInSeconds }) => {
                const seconds = currentTime - startTimeInSeconds;
                return seconds > 0 && seconds < durationInSeconds;
            }));
            setMarkersToShow(data.markers.filter(({ durationInSeconds, startTimeInSeconds }) => {
                const seconds = currentTime - startTimeInSeconds;
                return seconds > 0 && seconds < durationInSeconds;
            }));
        }, 100);
        return () => clearInterval(interval);
    }, [data]);

    return <Box maxWidth="lg" margin='auto'>
        {isLoading
            ? <><Skeleton height={100} /> <Skeleton height={150} /></>
            : status !== 200 ? <Alert severity="error">Coś poszło nie tak</Alert> : <>
                <Typography variant="h3" p={2}>{data.title}</Typography>
                <Box>
                    {markersToShow.map(marker => <MovieMarkerPin key={marker.id} marker={marker}/>)}
                    <video controls width='100%' id="video">
                        <source src={data.url} />
                    </video>
                    <Box position='relative' mt='-10%' mb='10%'>
                        <Box position='absolute' width='100%'>
                            {captionsToShow.map(caption =>
                                <div
                                    key={caption.id}
                                    style={{
                                        fontSize: '1.5rem',
                                        padding: 3, textAlign: 'center',
                                        backgroundColor: 'rgba(0, 0, 0, 0.5)',
                                        color: 'white',
                                        marginBottom: 1
                                    }}>{caption.text}</div>)}
                        </Box>
                    </Box>
                </Box>
                <Typography p={2}>{data.description}</Typography>
            </>
        }

    </Box>

}

export default MoviePage;
