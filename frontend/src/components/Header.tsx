"use client";

import { me } from "@/service/LoginService";
import { Dialog, DialogPanel } from "@headlessui/react";
import { Bars3Icon, XMarkIcon } from "@heroicons/react/24/outline";
import Image from "next/image";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function Header() {
    const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
    const [isLogin, setIsLogin] = useState(false);

    useEffect(() => {
        async function fetchData() {
            try {
                const response = await me();
                if (response.ok) {
                    setIsLogin(true);
                } else {
                    setIsLogin(false);
                }
            } catch (e) {
                console.log(`Error : ${e}`);
                setIsLogin(false);
            }
        }
        fetchData();
    }, []);

    return (
        <header className="bg-white">
            <nav className="mx-auto flex max-w-7xl items-center justify-between p-6 lg:px-8" aria-label="Global">
                <div className="flex lg:flex-1">
                    <Link href='/' className="-m-1.5 p-1.5">
                        <span className="h-9 w-auto font-bold sm:text-xl">Simple Board</span>
                    </Link>
                </div>
                <div className="flex lg:hidden">
                    <button
                        type="button"
                        className="-m-2.5 inline-flex items-center justify-center rounded-md p-2.5 text-gray-700"
                        onClick={() => setMobileMenuOpen(true)}
                    >
                        <span className="sr-only">Open main menu</span>
                        <Bars3Icon className="h-6 w-6" aria-hidden="true" />
                    </button>
                </div>
                <div className="hidden lg:flex lg:gap-x-12">
                    <Link href='/' className="text-sm font-semibold leading-6 text-gray-900">Home</Link>
                    <Link href='/articles/search-hashtags' className="text-sm font-semibold leading-6 text-gray-900">Hashtags</Link>
                </div>
                <div className="hidden lg:flex lg:flex-1 lg:justify-end gap-5">
                    {(!isLogin) ?
                        <>
                            <Link href='/login' className="text-sm font-semibold leading-6 text-gray-900">Login</Link>
                            <Link href="http://localhost:8080/oauth2/authorization/kakao">
                                <Image alt='kakao login' src='/images/kakao_login_small.png' width={50} height={20} />
                            </Link>
                        </> :
                        <Link href='/logout' className="text-sm font-semibold leading-6 text-gray-900">Logout</Link>
                    }
                </div>
                <Dialog className="lg:hidden" open={mobileMenuOpen} onClose={setMobileMenuOpen}>
                    <div className="flex items-center justify-between">
                        <DialogPanel className="fixed inset-y-0 right-0 z-10 w-full overflow-y-auto bg-white px-6 py-6 sm:max-w-sm sm:ring-1 sm:ring-gray-900/10">
                            <div className="flex items-center justify-between">
                                <Link href="/" className="-m-1.5 p-1.5">
                                    <span className="sr-only">Simple Board</span>
                                </Link>
                                <button className="rounded-md text-gray-700"
                                    onClick={() => setMobileMenuOpen(false)}
                                >
                                    <span className="sr-only">Close</span>
                                    <XMarkIcon className="h-6 w-6" aria-hidden="true" />
                                </button>
                            </div>
                            <div className="mt-6 flow-root">
                                <div className="-my-6 divide-y divide-gray-500/10">
                                    <Link href="/" className="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50">Home</Link>
                                    <Link href="/search-hashtag" className="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50">hashtag</Link>
                                </div>
                            </div>
                            <div className="py-6 mt-10">
                                {(!isLogin) ?
                                    <div className="flex gap-2">
                                        <Link href='/login' className="text-sm font-semibold leading-6 text-gray-900">Login</Link>
                                        <Image alt='kakao login' src='/images/kakao_login_small.png' width={50} height={20} />
                                    </div> :
                                    <Link href='/logout' className="text-sm font-semibold leading-6 text-gray-900">Logout</Link>
                                }
                            </div>
                        </DialogPanel>
                    </div>
                </Dialog>
            </nav>
        </header>
    );
}