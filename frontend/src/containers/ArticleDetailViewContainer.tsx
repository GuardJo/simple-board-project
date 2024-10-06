"use client"

import ArticleCommentList from "@/components/ArticleCommentList";
import ArticleContentForm from "@/components/ArticleContentForm";
import ArticleInfo from "@/components/ArticleInfo";
import BasicCard from "@/components/BasicCard";
import Button from "@/components/Button";
import { ArticleDetailInfo } from "@/interface";
import { useRouter } from "next/navigation";

export default function ArticleDetailViewContainer({articleId, articleDetail}: ArticleDetailViewContainerParams) {
    const router = useRouter();
    const { article, comments, isOwner }: ArticleDetailInfo = articleDetail;

    const handleMoveUpdateViewPage = () => {
        router.push("/update-view");
    }

    return (
        <div className="flex justify-center gap-3 px-24 py-10">
            <div className="flex flex-col gap-3 h-full" style={{width: "50%"}}>
                <BasicCard title={article.title}>
                    <ArticleContentForm content={article.content}></ArticleContentForm>
                </BasicCard>
                <BasicCard title={'댓글 (' + comments.length + ')'}>
                    <ArticleCommentList data={comments} />
                </BasicCard>
            </div>
            <div className="flex flex-col h-96 gap-3" style={{width: "30%"}}>
                <BasicCard title="게시글 정보">
                    <ArticleInfo author={article.creator} createdTime={article.createTime} hashtagNames={article.hashtags.map(hashtag => hashtag.hashtagName)}/>
                </BasicCard>
                {(isOwner) ? <Button onClick={() => {handleMoveUpdateViewPage()}}>수정</Button> : null}
            </div>
        </div >
    )
}

interface ArticleDetailViewContainerParams {
    articleId: number,
    articleDetail: ArticleDetailInfo
}