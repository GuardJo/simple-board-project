import SearchBar from "@/components/SearchBar";
import { Metadata } from "next";
import { Suspense } from "react";
import ArticleTable from "@/components/ArticleTable";
import PaginationBar from "@/components/PaginationBar";
import { getArticlePage } from "@/service/ArticleSearchService";
import Loading from "../loading";
import Button from "@/components/Button";
import Link from "next/link";

export const metadata: Metadata = {
  title: "게시판",
}

export default async function ArticleListPage({ searchParams }: ArticleQueryParams) {
  const { page, searchType, searchValue } = searchParams;
  const articles = await getArticlePage(page ?? 1, searchType ?? "TITLE", searchValue ?? "");

  return (
    <div className="flex flex-auto flex-col justify-center items-center gap-5">
      <SearchBar />
      <Suspense fallback={Loading()}>
        <ArticleTable data={articles} />
        <div className="flex justify-end lg:w-[1000px]">
          <Link href="/articles/create-view">
            <Button>게시글 작성</Button>
          </Link>
        </div>
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