import ArticleCommentList from "@/components/ArticleCommentList"
import ArticleContentForm from "@/components/ArticleContentForm"
import ArticleInfo from "@/components/ArticleInfo"
import BasicCard from "@/components/BasicCard"
import { ArticleDetailInfo } from "@/interface"
import { getArticleDetail } from "@/service/ArticleService"
import { Metadata } from "next"

export const metadata: Metadata = {
    title: "게사글 상세"
}

export default async function ArticleDetailPage({ params: { articleId } }: PathVariable) {

    const { article, comments }: ArticleDetailInfo = await getArticleDetail(articleId);

    return (
        <div className="flex justify-center content-start gap-3 px-24 py-10">
            <div className="flex flex-col gap-3 w-2/3 h-full">
                <BasicCard title={article.title}>
                    <ArticleContentForm content={article.content}></ArticleContentForm>
                </BasicCard>
                <BasicCard title={'댓글 (' + comments.length + ')'}>
                    <ArticleCommentList data={comments} />
                </BasicCard>
            </div>
            <div className="flex w-1/3 h-96">
                <BasicCard title="게시글 정보">
                    <ArticleInfo author={article.creator} createdTime={article.createTime} hashtagNames={article.hashtags.map(hashtag => hashtag.hashtagName)} />
                </BasicCard>
            </div>
        </div >
    )
}

interface PathVariable {
    params: {
        articleId: number,
    },
};