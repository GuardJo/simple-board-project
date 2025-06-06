"use client"

import BasicCard from "@/components/BasicCard";
import BasicButton from "@/components/BasicButton";
import TextInput from "@/components/TextInput";
import {login} from "@/service/LoginService";
import {useState} from "react";

export default function LoginPage() {
    const [userId, setUserId] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = async () => {
        const response = await login({username: userId, password});

        if (response) {
            window.location.href = "/articles?page=1"
        } else {
            alert("로그인에 실패하였습니다.");
        }
    }

    return (
        <div className="flex justify-center content-start gap-3 px-24 py-10">
            <div className="flex flex-col gap-3 w-2/5 h-full">
                <BasicCard title="로그인">
                    <TextInput labelName="아이디" placeholder="아이디를 입력해 주세요." onChange={setUserId}></TextInput>
                    <TextInput labelName="비밀번호" placeholder="비밀번호를 입력해 주세요." isSecret
                               onChange={setPassword}></TextInput>
                </BasicCard>
                <BasicButton onClick={handleLogin}>로그인</BasicButton>
            </div>
        </div>
    );
}