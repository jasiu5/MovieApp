import React, { useEffect } from 'react'
import { useNavigate } from 'react-router-dom';

import Button from '@mui/material/Button';
import Alert from '@mui/material/Alert';
import Divider from "@mui/material/Divider";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';

import useAPIFetch, { ENDPOINT } from '../useFetch';
import { user } from '../user';
import Pages from '.';

const LoginPage = () => {
    const navigate = useNavigate();
    const [execute, status, data, isLoading] = useAPIFetch<SignInResponseData>(ENDPOINT.SIGN_IN, "POST");

    useEffect(() => {
        if (status === 200 && !isLoading) {
            user.signIn(data);
            navigate(Pages.HOME.path);
        }
    }, [status, isLoading]);

    const handleSubmit = (event: { preventDefault: () => void; currentTarget: HTMLFormElement | undefined; }) => {
        event.preventDefault();
        const formData = new FormData(event.currentTarget);
        execute(Object.fromEntries(formData.entries()));
    }

    return (
        <Container component="main" maxWidth="sm">
            <Box
                sx={{
                    marginTop: 12,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                }}>

                <Typography
                    variant='h5'
                    fontWeight='bold'
                >
                    Logowanie
                </Typography>

                <Box component="form" onSubmit={handleSubmit}>
                    {status === 400 && <Alert severity="error">Niepoprawne dane logowania</Alert>}
                    <TextField
                        fullWidth
                        required
                        margin="normal"
                        label="Adres email"
                        name="email"
                    />
                    <TextField
                        fullWidth
                        required
                        margin="normal"
                        label="Hasło"
                        type="password"
                        name="password"
                    />
                    <Button
                        fullWidth
                        sx={{ mt: 3 }}
                        variant="contained"
                        type="submit"
                        disabled={isLoading}>
                        {
                            isLoading ? "Ładowanie..." : "Zaloguj się"
                        }
                    </Button>
                </Box>
            </Box>
            <Divider variant='fullWidth' sx={{ width: '100%', mt: 15, mb: 5 }}></Divider>
            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                }}>

                <Typography
                    variant='h6'
                    fontWeight='bold'
                >
                    Nie masz konta?
                </Typography>
                <Button
                    fullWidth
                    sx={{ mt: 3 }}
                    variant="contained"
                    onClick={() => navigate(Pages.SIGNUP.path)}>
                    Przejdź do rejestracji
                </Button>
            </Box>
        </Container>
    )
}

export default LoginPage;
