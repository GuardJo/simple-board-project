"use client"

import ArticleContentForm from "@/components/ArticleContentForm";
import BasicCard from "@/components/BasicCard";
import Button from "@/components/Button";
import TextInput from "@/components/TextInput";
import {createArticle, updateArticle} from "@/service/ArticleUpdateService";
import {me} from "@/service/LoginService";
import {useRouter} from "next/navigation";
import {useEffect, useState} from "react";

export default function ArticleUpdateFormContainer({
                                                       articleId,
                                                       title = "",
                                                       content = ""
                                                   }: ArticleUpdateFormContainerParams) {
    const [titleValue, setTitleValue] = useState(title);
    const [contentValue, setContentValue] = useState(content);
    const router = useRouter();

    useEffect(() => {
        async function checkLogin() {
            const response = await me();

            if (!response.ok) {
                router.push("/login");
            }
        }

        checkLogin();
    }, []);

    const handleUpdateArticle = async () => {
        if (articleId !== undefined) {
            await updateArticle({
                id: articleId,
                title: titleValue,
                content: contentValue,
            });
        } else {
            await createArticle({
                title: titleValue,
                content: contentValue,
            });
        }
        router.push("/");
    };

    const handleTitleValue = (arg: string) => {
        setTitleValue(arg);
    };

    return (
        <div className="flex justify-center items-center flex-col gap-3 px-24 py-10">
            <div className="flex flex-col gap-3 h-full" style={{width: "50%"}}>
                <BasicCard title={(articleId === undefined) ? '신규 게시글' : '게시글 수정'}>
                    <TextInput labelName="제목" onChange={handleTitleValue} content={titleValue}
                               placeholder="제목을 입력해 주세요."></TextInput>
                    <ArticleContentForm onChange={setContentValue} canWrite content={contentValue}></ArticleContentForm>
                </BasicCard>
                <div className="flex justify-end">
                    <Button onClick={handleUpdateArticle}>저장</Button>
                </div>
            </div>
        </div>
    );
}

interface ArticleUpdateFormContainerParams {
    articleId?: number,
    title?: string,
    content?: string,
};