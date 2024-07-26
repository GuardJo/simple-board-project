import { Metadata } from "next"

export const metadata: Metadata = {
    title: "게사글 상세"
}

export default function ArticleDetailPage({ params: { articleId } }: PathVariable) {
    // TODO 페이지 구현하기
    return (
        <div className="flex flex-auto flex-col justify-center items-center gap-5">
            <h2>{articleId}</h2>
        </div>
    )
}

interface PathVariable {
    params: {
        articleId: number,
    },
};