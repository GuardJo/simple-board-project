import {cookies} from "next/headers";

const baseUrl = "http://localhost:8080/api/v2";

export async function getArticlePage(page: number, searchType: string, searchValue: string) {
    const response = await fetch(`${baseUrl}/articles?page=${page - 1}&searchType=${searchType}&searchValue=${searchValue}`, {
        cache: 'no-store',
    });

    return response.json();
}

export async function getArticleDetail(articleId: number) {
    const response = await fetch(`${baseUrl}/articles/${articleId}`, {
        method: 'GET',
        cache: 'no-store',
        headers: {
            "Content-Type": "application/json",
            "Cookie": cookies().toString(),
        },
    });

    return response.json();
}