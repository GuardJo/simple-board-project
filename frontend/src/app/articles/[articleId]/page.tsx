import ArticleCommentList from "@/components/ArticleCommentList"
import ArticleContentForm from "@/components/ArticleContentForm"
import ArticleInfo from "@/components/ArticleInfo"
import BasicCard from "@/components/BasicCard"
import { CommentInfo } from "@/interface"
import { Metadata } from "next"

export const metadata: Metadata = {
    title: "게사글 상세"
}

export default function ArticleDetailPage({ params: { articleId } }: PathVariable) {

    const comments: CommentInfo[] = [
        { id: 1, author: "tester1", createdTime: "2024-07-31 00:00", content: "test" },
        { id: 2, author: "tester2", createdTime: "2024-07-31 00:02", content: "test2222" }
    ]
    return (
        <div className="flex justify-center content-start gap-3 px-24 py-10">
            <div className="flex flex-col gap-3 w-2/3 h-full">
                <BasicCard title="게시글 제목">
                    <ArticleContentForm content="본문 내용"></ArticleContentForm>
                </BasicCard>
                <BasicCard title="댓글">
                    <ArticleCommentList data={comments} />
                </BasicCard>
            </div>
            <div className="flex w-1/3 h-96">
                <BasicCard title="게시글 정보">
                    <ArticleInfo author="Tester" createdTime="2024-07-30 13:00" hashtagName="testTag" />
                </BasicCard>
            </div>
        </div>
    )
}

interface PathVariable {
    params: {
        articleId: number,
    },
};