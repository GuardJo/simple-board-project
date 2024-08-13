import { Meta, StoryObj } from '@storybook/react';
import TextInput from './../components/TextInput';
import { action } from '@storybook/addon-actions';
import { ChangeEvent } from 'react';

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
        onChange: (e: ChangeEvent<HTMLInputElement>) => {
            action("handleChange")(`Changed Value : ${e.target.value}`);
        }
    },
};
