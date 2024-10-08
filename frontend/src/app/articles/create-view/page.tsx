"use client"

import ArticleContentForm from "@/components/ArticleContentForm";
import BasicCard from "@/components/BasicCard";
import Button from "@/components/Button";
import TextInput from "@/components/TextInput";
import { createArticle } from "@/service/ArticleService";
import { me } from "@/service/LoginService";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function CreateView() {
    const [titleValue, setTitleValue] = useState("");
    const [contentValue, setContentValue] = useState("");
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

    const saveArticle = async () => {
        await createArticle({
            title: titleValue,
            content: contentValue,
        });
        router.push("/");
    };

    return (
        <div className="flex justify-center content-start gap-3 px-24 py-10">
            <div className="flex flex-col gap-3 w-2/3 h-full">
                <BasicCard title="신규 게시글">
                    <TextInput labelName="제목" onChange={setTitleValue} placeholder="제목을 입력해 주세요."></TextInput>
                    <ArticleContentForm onChange={setContentValue} canWrite></ArticleContentForm>
                </BasicCard>
                <div className="flex justify-end">
                    <Button onClick={saveArticle}>저장</Button>
                </div>
            </div>
        </div>
    );
}