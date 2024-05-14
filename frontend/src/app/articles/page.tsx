import SearchBar from "@/components/SearchBar";
import { Metadata } from "next";
import { Suspense } from "react";
import loading from "../loading";
import ArticleTable from "@/components/ArticleTable";
import PaginationBar from "@/components/PaginationBar";
import { getArticlePage } from "@/service/ArticleService";

export const metadata: Metadata = {
  title: "게시판",
}

export default async ({ searchParams }: ArticleQueryParams) => {
  const { page, searchType, searchValue } = searchParams;
  const articles = await getArticlePage(page ?? 1, searchType ?? "TITLE", searchValue ?? "");

  return (
    <div className="flex flex-auto flex-col justify-center items-center gap-5">
      <SearchBar />
      <Suspense fallback={loading()}>
        <ArticleTable data={articles} />
        <PaginationBar number={articles.number} totalPage={articles.totalPage} searchType={searchType} searchValue={searchValue} />
      </Suspense>
    </div>
  );
}

interface ArticleQueryParams {
  searchParams: {
    page?: number,
    searchType?: string,
    searchValue?: string,
  },
};