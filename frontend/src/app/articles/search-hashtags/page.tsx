import { Suspense } from "react";
import Loading from './../../loading';
import ArticleTable from "@/components/ArticleTable";
import { getArticlePage } from "@/service/ArticleService";
import PaginationBar from "@/components/PaginationBar";
import { ArticleList, HashtagInfo } from "@/interface";
import HashtagList from "@/components/HashtagList";
import { getHashTagList } from "@/service/HashtagService";
import { Metadata } from "next";

export const metadata: Metadata = {
    title: "해시태그 검색"
}

export default async function getSearchHashtagPage({ searchParams }: SearchHashtagParams) {
    const { page, searchValue } = searchParams;
    const hashtagList: HashtagInfo[] = getHashTagList();
    const searchArticles: ArticleList = await getArticlePage(page ?? 1, "HASHTAG", searchValue ?? "");
    return (
        <div className="flex flex-auto flex-col justify-center items-center gap-5">
            <HashtagList data={hashtagList}></HashtagList>
            <Suspense fallback={Loading()}>
                <ArticleTable data={searchArticles}></ArticleTable>
                <PaginationBar isHashTagSearch number={searchArticles.number} totalPage={searchArticles.totalPage} searchType="HASHTAG" searchValue={searchValue ?? ""}></PaginationBar>
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