"use client"

import ArticleCommentList from "@/components/ArticleCommentList";
import ArticleContentForm from "@/components/ArticleContentForm";
import ArticleInfo from "@/components/ArticleInfo";
import BasicCard from "@/components/BasicCard";
import BasicButton from "@/components/BasicButton";
import {ArticleDetailInfo} from "@/interface";
import {useEffect, useState} from "react";
import ArticleUpdateFormContainer from "./ArticleUpdateFormContainer";

export default function ArticleDetailViewContainer({articleId, articleDetail}: ArticleDetailViewContainerParams) {
    const [isUpdateMode, setIsUpdateMode] = useState(false);
    const {article, comments, isOwner}: ArticleDetailInfo = articleDetail;

    const handleUpdateMode = (updateMode: boolean) => {
        setIsUpdateMode(updateMode);
    }

    useEffect(() => {
    }, [isUpdateMode]);

    return (
        <>
            {isUpdateMode
                ?
                <ArticleUpdateFormContainer articleId={articleId} title={article.title} content={article.content}/>
                :
                <div className="flex justify-center gap-3 px-24 py-10">
                    <div className="flex flex-col gap-3 h-full" style={{width: "50%"}}>
                        <BasicCard title={article.title}>
                            <ArticleContentForm content={article.content}></ArticleContentForm>
                        </BasicCard>
                        <BasicCard title={'댓글 (' + comments.length + ')'}>
                            <ArticleCommentList data={comments}/>
                        </BasicCard>
                    </div>
                    <div className="flex flex-col h-96 gap-3" style={{width: "30%"}}>
                        <BasicCard title="게시글 정보">
                            <ArticleInfo author={article.creator} createdTime={article.createTime}
                                         hashtagNames={article.hashtags.map(hashtag => hashtag.hashtagName)}/>
                        </BasicCard>
                        {(isOwner) ? <BasicButton onClick={() => {
                            handleUpdateMode(true)
                        }}>수정</BasicButton> : null}
                    </div>
                </div>}
        </>
    )
}

interface ArticleDetailViewContainerParams {
    articleId: number,
    articleDetail: ArticleDetailInfo
}