class User {
  token: string;
  email: string;

  constructor() {
    this.token = "";
    this.email = "";
  }

  signIn = (signInData: SignInResponseData) => {
    this.token = signInData.token;
    this.email = signInData.email;
  };

  signOut = () => {
    this.token = "";
    this.email = "";
  };

  isSignIn = () => this.token !== "";
}

export const user = new User();
