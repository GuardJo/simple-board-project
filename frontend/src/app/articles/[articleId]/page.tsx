import ArticleDetailViewContainer from "@/containers/ArticleDetailViewContainer"
import { ArticleDetailInfo } from "@/interface"
import { getArticleDetail } from "@/service/ArticleSearchService"
import { Metadata } from "next"

export const metadata: Metadata = {
    title: "게사글 상세"
}

export default async function ArticleDetailPage({ params: { articleId } }: PathVariable) {
    const articleDetail: ArticleDetailInfo = await getArticleDetail(articleId);

    return (
        <ArticleDetailViewContainer articleId={articleId} articleDetail={articleDetail}/>
    )
}

interface PathVariable {
    params: {
        articleId: number,
    },
};