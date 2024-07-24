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
    createTime: string,
};

export interface HashtagInfo {
    id: number,
    hashtagName: string,
}