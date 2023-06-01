import React, { useState } from "react";
import { user } from './user';

export const url = "http://localhost:8079";

export enum ENDPOINT {
  SIGN_UP = "django/registration",
  SIGN_IN = "django/login",
  MOVIE_SEARCH = "django/movie/search",
  MOVIE_GET_POST = "django/movie"
}

export type METHOD = "GET" | "POST" | "PATCH" | "DELETE";

export default function useAPIFetch<DataType>(
  endpoint: ENDPOINT,
  method: METHOD = "GET",
  isFormData: Boolean = false
): [
    (data: any, ...queryParams: Array<string | number>) => void,
    number,
    DataType,
    boolean
  ] {
  const [status, setStatus] = useState<number>(0);
  const [data, setData] = useState<DataType>();
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const execute = (data: any, ...queryParams: Array<string | number>) => {
    setIsLoading(true);

    (method === 'GET' ?
      fetch(`${url}/${endpoint}/${queryParams.join("/")}?${new URLSearchParams(data).toString()}`, {
        method: 'GET',
        headers: {
          "Content-Type": "application/json",
          Authorization: `Token ${user.token}`,
        }
      })
      :
      (isFormData ? fetch(`${url}/${endpoint}/${queryParams.join("/")}`, {
        method: method,
        headers: {
          Authorization: `Token ${user.token}`,
        },
        body: data,
      }) : fetch(`${url}/${endpoint}/${queryParams.join("/")}`, {
        method: method,
        headers: {
          "Content-Type": "application/json",
          Authorization: `Token ${user.token}`,
        },
        body: data !== undefined ? JSON.stringify(data) : data,
      })))
      .then((response) => {
        setStatus(response.status);
        return response.json();
      })
      .then((result) => {
        setData(result);
        setIsLoading(false);
      })
      .catch((error) => {
        console.error("Error:", error);
        setIsLoading(false);
      });
  };

  return [execute, status, data as DataType, isLoading];
}
