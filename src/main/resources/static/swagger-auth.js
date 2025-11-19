window.onload = () => {
    const token = localStorage.getItem("jwt");
    if (token) {
        ui.preauthorizeApiKey("bearerAuth", token);
    }
};
