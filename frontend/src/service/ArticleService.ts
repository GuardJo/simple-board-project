import { ArticleDetailInfo } from "@/interface";

const baseUrl = "http://localhost:8080/api/v2";

export async function getArticlePage(page: number, searchType: string, searchValue: string) {
    const response = await fetch(`${baseUrl}/articles?page=${page - 1}&searchType=${searchType}&searchValue=${searchValue}`, {
        cache: 'no-store',
    });

    return response.json();
}

export function getArticleDetail(articleId: number): ArticleDetailInfo {
    // TODO API 연동하기
    return {
        article: {
            id: 1,
            title: "게시글 제목",
            creator: "게시글 작성자",
            content: "게시글 내용",
            createTime: "2024-07-31 00:18",
            hashtags: [
                {
                    id: 1,
                    hashtagName: "Tag",
                },
            ],
        },
        comments: [
            {
                id: 1,
                author: "댓글 작성자",
                createdTime: "2024-07-31 12:19",
                content: "댓글 내용",
            },
            {
                id: 2, 
                author: "tester2", 
                createdTime: "2024-07-31 00:02",
                 content: "test2222"
            },
        ],
    };
}