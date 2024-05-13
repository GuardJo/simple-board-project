export interface ArticleList {
    number: number,
    totalPage: number,
    articles: ArticleInfo[],
};

export interface ArticleInfo {
    id: number,
    title: string,
    content: string,
    creator: string,
    hashtags: HashtagInfo[],
    createtime: string,
};

export interface HashtagInfo {
    id: number,
    name: string,
}