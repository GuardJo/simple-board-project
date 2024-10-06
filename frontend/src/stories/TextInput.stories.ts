import { Meta, StoryObj } from '@storybook/react';
import TextInput from './../components/TextInput';
import { action } from '@storybook/addon-actions';

const meta = {
    title: "components/TextInput",
    component: TextInput,
} satisfies Meta<typeof TextInput>

export default meta;
type Story = StoryObj<typeof TextInput>;

export const Default: Story = {
    args: {
        labelName: "제목",
        placeholder: "제목을 입력해 주세요.",
        onChange: (arg: string) => (
            action("handleChange")(`Changed Value : ${arg}`)
        ),
    },
};

export const Password: Story = {
    args: {
        labelName: "비밀번호",
        placeholder: "비밀번호를 입력해 주세요.",
        onChange: (arg: string) => (
            action("handleChange")(`Changed Value : ${arg}`)
        ),
        isSecret: true,
    },
};
