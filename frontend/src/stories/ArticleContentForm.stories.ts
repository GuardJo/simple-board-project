import { Meta, StoryObj } from "@storybook/react";
import ArticleContentForm from '../components/ArticleContentForm';
import {action} from '@storybook/addon-actions';
import { ChangeEvent } from "react";

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

const handleChange = (e: ChangeEvent<HTMLTextAreaElement>) => {
    action("handleChange")(`Changed Value : ${e.target.value}`);
}

export const CanWrite: Story = {
    args: {
        canWrite: true,
        onChange: handleChange,
    },
};