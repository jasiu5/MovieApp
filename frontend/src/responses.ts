type SignInResponseData = {
    token: string,
    email: string
}

type SignUpResponseData = SignInResponseData;
type MovieSearchResponseData = Array<Movie>;
type MovieGetResponseData = MovieDetails;

type Movie = {
    id?: number,
    title: string,
    description: string,
}

type MovieDetails = Movie & {
    url: string
    captions: Array<Caption>,
    markers: Array<Marker>
}

type Caption = {
    id?: number,
    startTimeInSeconds: number,
    durationInSeconds: number,
    text: string
}

type Marker = {
    id?: number,
    startTimeInSeconds: number,
    durationInSeconds: number,
    text: string,
    positionX: number,
    positionY: number
}
