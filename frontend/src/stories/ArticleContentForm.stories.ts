import { Meta, StoryObj } from "@storybook/react";
import ArticleContentForm from '../components/ArticleContentForm';

const meta = {
    title: "components/ArticleContentForm",
    component: ArticleContentForm,
} satisfies Meta<typeof ArticleContentForm>

export default meta;

type Story = StoryObj<typeof ArticleContentForm>

export const NoContents: Story = {
    args: {}
};

export const HasContents:Story = {
    args: {
        content: "테스트 게시글",
    },
};

export const CanWrite: Story = {
    args: {
        canWrite: true,
    },
};