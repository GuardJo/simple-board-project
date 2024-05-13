import ArticleTable from "../components/ArticleTable";
import { Meta, StoryObj } from "@storybook/react";

const meta = {
    title: "components/ArticleTable",
    component: ArticleTable,
} satisfies Meta<typeof ArticleTable>

const articles = {
    page: {
        number: 0,
        totalPage: 10,
        totalElements: 100,
        size: 10,
    },
    _embedded: [
        {
            id: 1,
            title: "Test1",
            creator: "Tester1",
            hashtags: [],
            createtime: "2024-05-13 10:47",
        },
    ],
} satisfies Partial<ArticleList>

export default meta;
type Story = StoryObj<typeof ArticleTable>

export const NoData: Story = {
    args: {},
};

export const Default: Story = {
    args: {
        data: articles,
    }
}