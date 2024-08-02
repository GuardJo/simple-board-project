import { Meta, StoryObj } from "@storybook/react";
import ArticleComment from './../components/ArticleComment';

const meta = {
    title: "components/ArticleComment",
    component: ArticleComment,
} satisfies Meta<typeof ArticleComment>;

export default meta;
type Story = StoryObj<typeof ArticleComment>;

export const Default:Story = {
    args: {
        author: "테스터",
        updatedAt: "2024-07-28 23:55",
        content: "테스트 댓글"
    },
};