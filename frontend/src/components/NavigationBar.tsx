import React from "react";

import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Button from "@mui/material/Button";
import { user } from "../user";
import { useNavigate } from "react-router-dom";
import Pages from "../pages";

const NavigationBar = () => {
    const navigate = useNavigate();

    const handleSignOut = () => {
        user.signOut();
        navigate(Pages.SIGNIN.path);
    }

    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="static">
                <Toolbar>
                    <div style={{ flexGrow: 1 }}>
                        <Button color="inherit" onClick={() => navigate(Pages.HOME.path)}>{Pages.HOME.name}</Button>
                        {user.isSignIn() && <Button color="inherit" onClick={() => navigate(Pages.MY_MOVIES.path)}>{Pages.MY_MOVIES.name}</Button>}
                    </div>
                    {
                        user.isSignIn()
                        ?
                        <Button color="inherit" onClick={handleSignOut}>Wyloguj się</Button>
                        :
                        <>
                            <Button color="inherit" onClick={() =>navigate(Pages.SIGNUP.path)}>Rejestracja</Button>
                            <Button color="inherit" onClick={() =>navigate(Pages.SIGNIN.path)}>Logowanie</Button>
                            </>
                    }
                </Toolbar>
            </AppBar>
        </Box>
    );
}

export default NavigationBar;
