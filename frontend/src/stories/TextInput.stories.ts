import { Meta, StoryObj } from '@storybook/react';
import TextInput from './../components/TextInput';

const meta = {
    title: "components/TextInput",
    component: TextInput,
} satisfies Meta<typeof TextInput>

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        labelName: "제목",
        placeholder: "제목을 입력해 주세요.",
    },
};
