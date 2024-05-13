interface ArticleList {
    page: {
        size: number,
        totalElements: number,
        totalPage: number,
        number: number,
    },
    _embedded: ArticleInfo[],
};

interface ArticleInfo {
    id: number,
    title: string,
    creator: string,
    hashtags: HashtagInfo[],
    createtime: string,
};

interface HashtagInfo {
    id: number,
    name: string,
}