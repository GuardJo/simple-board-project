import { Suspense } from "react";
import Loading from './../../loading';
import ArticleTable from "@/components/ArticleTable";

export default function getSearchHashtagPage({ searchParams }: SearchHashtagParams) {
    return (
        <div className="flex flex-auto flex-col justify-center items-center gap-5">
            <p>해시태그 목록 보여줄것</p>
            <Suspense fallback={Loading()}>
                <ArticleTable></ArticleTable>
            </Suspense>
        </div>
    )
}

interface SearchHashtagParams {
    searchParams: {
        page?: number,
        searchValue?: string,
    },
}